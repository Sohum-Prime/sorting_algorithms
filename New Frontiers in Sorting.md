# New Challenges and Algorithmic Innovations in Sorting Problems

## Introduction

Sorting is a fundamental operation in computer science, classically considered a solved problem 
with well-known algorithms (e.g. quicksort, mergesort) reaching the optimal $O(n \log n)$ 
comparison complexity. However, emerging computing paradigms and application domains continue 
to pose new challenges in sorting. Modern hardware like GPUs and multi-core processors, enormous 
datasets in distributed systems, and even artificial intelligence methods for algorithm discovery 
have all spurred active research into novel sorting variants and optimisations. The goal is often 
not to beat the theoretical complexity, but to improve practical performance under new constraints 
or to discover more efficient routines within those bounds. In this report, I am exploring some of 
these frontier problems in sorting and delving into one in detail – an AI-driven approach (AlphaDev) 
to discovering faster sorting algorithms. I will explain what this problem entails and summarise 
the AlphaDev paper’s contributions to the topic.

## Sorting on Modern Hardware

One area of active research is designing sorting algorithms tailored for massively parallel 
hardware, such as graphics processing units (GPUs) and distributed cloud systems. Traditional 
sorting algorithms optimised for single-core CPUs may perform suboptimally on parallel 
architectures due to factors like memory access patterns and thread divergence. For instance, 
GPU-friendly sorting methods often rely on sorting networks (like bitonic sort) or radix sort 
to exploit parallelism. A sorting network uses fixed sequences of compare-exchange operations 
that can be executed in parallel layers, which maps well to GPU hardware. However, naive 
implementations (e.g. a straightforward bitonic sort) might require $O(n \log^2 n)$ comparisons 
and excessive memory bandwidth, so HPC (high performance computing) researchers are seeking 
improvements that reduce overhead and better utilise the GPU memory hierarchy.

State-of-the-art GPU sorting algorithms combine algorithmic cleverness with low-level optimisation. 
A recent example is OneSweep, a GPU radix sort that significantly improves throughput by reducing 
global memory passes [(Onesweep)](https://arxiv.org/abs/2206.01784). Classic least-significant-digit 
(LSD) radix sorts process each digit (or bit group) of keys with multiple passes over the data; 
OneSweep introduces a single-pass technique for the key-bin prefix sum, requiring only ~2n global 
memory operations per digit instead of ~3n in previous approaches. This reduction in memory traffic 
yields impressive speed-ups. On an NVIDIA A100 GPU, OneSweep achieved 29.4 billion keys per second 
sorting 256 million 32-bit keys – about 1.5× faster than the CUB library’s highly optimised radix 
sort. This illustrates that even though sorting is a classic problem, hardware-specific constraints 
(like memory bandwidth and parallel thread utilisation) create ongoing opportunities for algorithmic advances.

## AI-Discovered Sorting Algorithms (Autonomous Algorithm Discovery)

Another exciting frontier is the use of artificial intelligence to discover improved sorting 
algorithms. Instead of humans hand-crafting a new sort routine, can an AI agent autonomously 
find a faster way to sort? This question lies at the heart of a recent 
line of research that treats algorithm design as a search problem amenable to machine learning 
and heuristic search (this also plays a key role in determining if agentic systems can
generally contribute to useful science and math). The motivation is that decades of human optimisation have made popular 
sorting routines very efficient, and further manual tweaks yield diminishing returns.
AI offers a way to explore the vast space of possible programs beyond human intuition.

The specific variant of sorting we discuss here is finding new sorting algorithms via reinforcement 
learning. In 2023, researchers from DeepMind introduced AlphaDev, a system based on reinforcement 
learning (RL) that discovered improved sorting algorithms from scratch [(AlphaDev)](https://deepmind.google/discover/blog/alphadev-discovers-faster-sorting-algorithms/). 
The problem was framed as a single-player game (dubbed the “AssemblyGame”) where the agent’s task 
is to output a correct sorting program that minimises execution time [DRL for Sorting - Nature](https://www.nature.com/articles/s41586-023-06004-9?error=cookies_not_supported&code=816cc06f-2a7a-4305-9973-07f90c1a4721#:~:text=realize%20this%2C%20we%20formulated%20the,domains%2C%20showcasing%20the%20generality%20of). 
In each turn of the game, AlphaDev appends a low-level CPU instruction (an assembly opcode) to the 
candidate program and receives feedback (a reward) based on whether the program still sorts 
correctly and how fast it runs on actual hardware. Over many episodes, the RL agent learns to 
navigate the enormous search space of programs to find those that are both correct and efficient. 
Figure 1 illustrates the AlphaDev approach, where the agent incrementally builds an assembly 
program and is evaluated on sorting accuracy and speed.

![AlphaDev Figure 1](https://lh3.googleusercontent.com/E-P1x1eGUUkLta0e-eSj_tUZC6eyq3yIZbvYACIt61xhlm31J9NJlujj66_twj9m2rGb6sKTiK59W8mnoavj02j3YDYoRfzK_jPS3Sq_qJxMe7ztgg=w2140-rw)
Figure 1: AlphaDev’s assembly game formulation for sorting algorithm discovery. The agent observes 
the current state of the program and CPU, then selects the next assembly instruction to add. After 
each move, the partially built program is tested on all inputs of a given size (for example, all 
permutations of 3 elements) and rewarded based on output correctness and execution latency. This 
transforms sorting algorithm optimisation into a game, enabling the use of game-playing RL techniques.

The search space in this formulation is astronomically large – the number of possible instruction 
sequences is comparable to the number of particles in the universe (on the order of $10^{120}$ or 
more). Brute force is infeasible, so AlphaDev leverages advanced RL search (inspired by 
AlphaZero’s Monte Carlo Tree Search approach) with neural network guidance to efficiently explore 
and find optimal or near-optimal programs. The agent was restricted to a tailored set of x86 
assembly instructions (loads, stores, comparisons, conditional moves, etc.) sufficient for 
implementing sorting logic [Exploring the Sorting Space](https://medium.com/@charles.f.randall/exploring-the-problem-space-of-deepmind-alphadev-sorting-71035a8be1ab). 
Interestingly, by working at the assembly level, the AI could discover tricks that might be 
obscured in high-level code – taking advantage of low-level instruction behavior and CPU 
operation costs that a human writing C++ might not consider.

A key result of this approach was the discovery of new sorting routines for small arrays that 
outperform the best human-crafted equivalents. The AlphaDev agent succeeded in finding sorting 
programs for sequences of 3, 4, and 5 elements that use fewer instructions (and hence run faster) 
than the existing implementations in the standard C++ library. For example, the prior optimal 
routine for sorting 3 elements used 18 x86 instructions, whereas AlphaDev found a correct 
solution in only 17 instructions. This might seem like a minor difference, but when such a tiny 
routine is invoked trillions of times, eliminating even one instruction per call can yield 
significant performance and energy gains. In fact, the AlphaDev-discovered algorithms 
demonstrated up to a 70% speed-up for sorting very small arrays (length 5) compared to the 
old routines, and still about 1.7% improvement for large arrays (when the small sort is used 
as a subroutine in a divide-and-conquer sort. These improvements held across multiple data types 
and CPU architectures.

The AlphaDev study is a prominent example of Autonomous Scientific Discovery (ASD) in algorithms 
– where an AI system independently produces new scientific or engineering knowledge (in this case, 
a faster algorithm) beyond what humans have achieved. It combines deep theoretical ideas 
(searching program space with correctness and performance constraints) with practical impact. 
The result is not a new complexity class of sorting, but a concrete constant-factor improvement 
that matters at scale. It demonstrates that even problems long thought “essentially solved” like 
sorting still have room for innovation when viewed through a new lens.

## Broader Implications and Future Directions

On the hardware side, as GPUs, TPUs, and specialised accelerators become ubiquitous, algorithms must be revisited to exploit massive parallelism and cope with hierarchical memory. Research into parallel sorting (including multi-GPU and distributed sorting) is likely to remain active, ensuring that sorting of huge datasets can be done as efficiently as possible in cloud and edge computing environments. Techniques like radix sort (leveraging integer representations) and sample sort (for distributed data) will evolve alongside hardware advances.

On the AI side, the success of AlphaDev hints at a new paradigm of using AI agents or large models 
as “math agents” or algorithm designers. While current generative models (e.g. GPT-5, Qwen-3, and other 
foundation models) can write sorting code from specifications, they usually reproduce standard 
algorithms from training data and won’t automatically find novel optimisations. However, by 
integrating learning-based models with search (as AlphaDev did, or via techniques in program 
synthesis), we could see more instances of AI discovering improved algorithms or data structures. 
Notably, DeepMind’s earlier AlphaTensor used a similar approach to find better matrix 
multiplication algorithms, another domain where human progress had stalled.

## References

1. Daniel J. Mankowitz et al. (2023). "Faster sorting algorithms discovered using deep reinforcement learning." Nature, 618: 257–263. DOI: 10.1038/s41586-023-06004-9

2. DeepMind (2023). "AlphaDev discovers faster sorting algorithms." DeepMind Science Blog, 7 June 2023

3. Andy Adinets and Duane Merrill (2022). "OneSweep: A Faster Least Significant Digit Radix Sort for GPUs." arXiv:2206.01784 [cs.DC]

4. Charles Randall (2025). "Exploring the Problem Space of DeepMind AlphaDev Sorting (Part 1)." Medium, 28 Jan 2025
